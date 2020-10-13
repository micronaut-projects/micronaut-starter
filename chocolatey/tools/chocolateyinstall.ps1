$version = '2.1.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'FE45E6D45CAF4ED06C2E8D95F5E8F4496CE864B7BB1DABA268E16642FE8E5EBA'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
