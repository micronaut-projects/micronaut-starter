$version = '5.1.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'A16E7A2CF0258C2AC531172DFFA3273909A9F8960BBED7B044A589FE83B36875'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
