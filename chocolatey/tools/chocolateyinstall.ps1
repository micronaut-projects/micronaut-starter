$version = '4.10.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '6A2F864DECD8C08928492EF76549ABDCF4178C19F04480D275C502677827BCE6'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
