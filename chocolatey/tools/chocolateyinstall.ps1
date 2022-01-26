$version = '3.2.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'A11B7753C2C644030E9BFB1585F20DF574331BB3B8F79EA70721B826961F5694'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
