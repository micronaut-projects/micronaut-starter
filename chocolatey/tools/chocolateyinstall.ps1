$version = '2.4.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8A5E20DE39E8E626203753FC60EBCA0AB2F002A3D7D242020726409A9C381F70'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
