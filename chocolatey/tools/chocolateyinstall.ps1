$version = '2.0.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '3000C6BB42B92491199DF419A33B19DCA7B95566D6B7A554977F7327A104B70A'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs